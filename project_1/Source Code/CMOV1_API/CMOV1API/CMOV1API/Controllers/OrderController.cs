using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using CMOV1API.Business;
using CMOV1API.Data;
using CMOV1API.Model;
using CMOV1API.Requests;
using CMOV1API.Responses;
using CMOV1API.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace CMOV1API.Controllers
{
    [Produces("application/json")]
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class OrderController : ControllerBase
    {
        private const int BONUS_MULTIPLIER = 100;
        private readonly ApplicationContext _context;
        private readonly DatabaseErrorHandler _databaseErrorHandler;
        private readonly EncryptionHelper _encryptionHelper;

        public OrderController(ApplicationContext context, DatabaseErrorHandler databaseErrorHandler, EncryptionHelper encryptionHelper)
        {
            _context = context;
            _databaseErrorHandler = databaseErrorHandler;
            _encryptionHelper = encryptionHelper;
        }

        [HttpPost]
        public ActionResult<TotalPriceAcceptedVouchersResponse> PlaceOrder([FromBody] PlaceOrderRequest placeOrderRequest)
        {
            try
            {
                byte[] data;

                using (MemoryStream memoryStream = new MemoryStream())
                {
                    BinaryWriter binaryWriter = new BinaryWriter(memoryStream);
                    foreach (Guid voucherId in placeOrderRequest.VoucherIds)
                    {
                        binaryWriter.Write(voucherId.ToString().ToCharArray());
                    }
                    using (MemoryStream memoryStream2 = new MemoryStream())
                    {
                        BinaryWriter binaryWriter2 = new BinaryWriter(memoryStream2);
                        for (int i = placeOrderRequest.ProductIds.Count - 1; i >= 0; i--)
                        {
                            binaryWriter2.Write(placeOrderRequest.ProductIds[i]);
                        }
                        byte[] array2 = memoryStream2.ToArray();
                        Array.Reverse(array2);
                        binaryWriter.Write(array2);
                    }
                    binaryWriter.Write(placeOrderRequest.UserId.ToString().ToCharArray());
                    data = memoryStream.ToArray();
                }

                using (UnitOfWork unitOfWork = new UnitOfWork(_context))
                {
                    if (!_encryptionHelper.IsValidKey(data, placeOrderRequest.Signature, placeOrderRequest.UserId, unitOfWork))
                    {
                        return Forbid("Invalid Signature");
                    }

                    if (placeOrderRequest.ProductIds.Count == 0)
                    {
                        return BadRequest("Must choose products");
                    }

                    if (placeOrderRequest.VoucherIds.Count > 2)
                    {
                        return BadRequest("Can only used at most 2 vouchers");
                    }

                    User user = unitOfWork.Users.GetByGuid(placeOrderRequest.UserId);
                    if (user == null)
                    {
                        return NotFound("User");
                    }

                    Order order = new Order
                    {
                        User = user,
                        PaidValue = 0,
                        Vouchers = new List<Voucher>(),
                        OrdersProducts = new List<OrderProduct>()
                    };

                    decimal globalDiscount = 0;
                    decimal freeCoffees = 0;
                    decimal freePopcorns = 0;
                    foreach (Guid voucherId in placeOrderRequest.VoucherIds)
                    {
                        Voucher voucher = unitOfWork.Vouchers.GetByGuid(voucherId);
                        if (voucher == null)
                        {
                            return NotFound("Voucher");
                        }

                        if (voucher.UserId != user.Id)
                        {
                            return BadRequest("User does not own voucher");
                        }

                        if (voucher.OrderId != null)
                        {
                            return BadRequest("Already used vouchers sent");
                        }

                        switch (voucher.Type)
                        {
                            case "5Cafeteria":
                                if (globalDiscount == 0)
                                {
                                    globalDiscount = 0.05m;
                                }
                                else
                                {
                                    return BadRequest("Only one 5Discount can be used per order");
                                }
                                break;

                            case "FreeCoffee":
                                freeCoffees++;
                                break;

                            case "FreePopcorn":
                                freePopcorns++;
                                break;

                            default:
                                return NotFound("Voucher Type");
                        }

                        voucher.Order = order;
                        order.Vouchers.Add(voucher);
                    }

                    foreach (int productId in placeOrderRequest.ProductIds)
                    {
                        Product product = unitOfWork.Products.Get(productId);
                        if (product == null)
                        {
                            return NotFound("Product");
                        }

                        OrderProduct orderProduct = order.OrdersProducts.FirstOrDefault(op => op.ProductId == productId);
                        if (orderProduct == null)
                        {
                            order.OrdersProducts.Add(new OrderProduct
                            {
                                Order = order,
                                Product = product,
                                ProductId = productId,
                                Quantity = 1
                            });
                        }
                        else
                        {
                            orderProduct.Quantity++;
                        }

                        bool free = false;
                        if (product.Name == "Coffee" && freeCoffees > 0)
                        {
                            freeCoffees--;
                            free = true;
                        }
                        else if (product.Name == "Popcorn" && freePopcorns > 0)
                        {
                            freePopcorns--;
                            free = true;
                        }

                        if (!free)
                        {
                            order.PaidValue += product.Price * (1 - globalDiscount);
                        }
                    }

                    // Retake Unused Vouchers
                    if (order.PaidValue == 0)
                    {
                        Voucher voucher = order.Vouchers.FirstOrDefault(v => v.Type == "5Cafeteria");
                        if (voucher != null)
                        {
                            order.Vouchers.Remove(voucher);
                            voucher.Order = null;
                        }
                    }

                    for (int i = 0; i < freeCoffees; i++)
                    {
                        Voucher voucher = order.Vouchers.FirstOrDefault(v => v.Type == "FreeCoffee");
                        if (voucher == null)
                        {
                            return StatusCode(500);
                        }
                        order.Vouchers.Remove(voucher);
                        voucher.Order = null;
                    }

                    for (int i = 0; i < freePopcorns; i++)
                    {
                        Voucher voucher = order.Vouchers.FirstOrDefault(v => v.Type == "FreeCoffee");
                        if (voucher == null)
                        {
                            return StatusCode(500);
                        }
                        order.Vouchers.Remove(voucher);
                        voucher.Order = null;
                    }

                    // Bonus Vouchers
                    decimal currentSpending = unitOfWork.Users.GetSpending(placeOrderRequest.UserId);
                    int currentSpendingMultiplier = (int)currentSpending / BONUS_MULTIPLIER;

                    decimal newSpending = currentSpending + order.PaidValue;
                    int newSpendingMultiplier = (int)newSpending / BONUS_MULTIPLIER;

                    int differenceMultiplier = newSpendingMultiplier - currentSpendingMultiplier;
                    for (int i = 0; i < differenceMultiplier; i++)
                    {
                        Voucher voucher = new Voucher
                        {
                            User = user,
                            Type = "5Cafeteria"
                        };

                        unitOfWork.Vouchers.Add(voucher);
                    }

                    // Add Order
                    unitOfWork.Orders.Add(order);

                    // Saving
                    try
                    {
                        unitOfWork.Complete();
                    }
                    catch (Exception)
                    {
                        return StatusCode(500);
                    }

                    TotalPriceAcceptedVouchersResponse totalPriceAcceptedVouchersResponse = new TotalPriceAcceptedVouchersResponse
                    {
                        OrderId = order.Id,
                        AcceptedVouchers = order.Vouchers.Select(v => new VoucherBE { Id = v.Id, Type = v.Type }).ToList(),
                        TotalPrice = order.PaidValue
                    };

                    return Ok(totalPriceAcceptedVouchersResponse);
                }
            }
            catch(Exception e)
            {
                return StatusCode(500, e.StackTrace);
            }
        }
    }
}