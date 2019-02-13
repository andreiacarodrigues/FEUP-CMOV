using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Threading.Tasks;
using CMOV1API.Business;
using CMOV1API.Data;
using CMOV1API.Model;
using CMOV1API.Requests;
using CMOV1API.Responses;
using CMOV1API.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace CMOV1API.Controllers
{
    [Produces("application/json")]
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly ApplicationContext _context;
        private readonly DatabaseErrorHandler _databaseErrorHandler;

        public UserController(ApplicationContext context, DatabaseErrorHandler databaseErrorHandler)
        {
            _context = context;
            _databaseErrorHandler = databaseErrorHandler;
        }

        [HttpPost]
        public ActionResult<User> Register([FromBody] User user)
        {
            using (UnitOfWork unitOfWork = new UnitOfWork(_context))
            {
                unitOfWork.Users.Add(user);

                try
                {
                    unitOfWork.Complete();
                }
                catch (DbUpdateException e)
                {
                    string result = _databaseErrorHandler.DbUpdateExceptionHandler(e);
                    if (result != "")
                    {
                        if(unitOfWork.Users.Find(u => u.Username == user.Username) != null)
                        {
                            return BadRequest("User with the same Username already exists");
                        }

                        return BadRequest(result);
                    }
                    else
                    {
                        return StatusCode(500);
                    }
                }
                catch (Exception)
                {
                    return StatusCode(500);
                }
                return Ok(user);
            }
        }

        [HttpPost]
        public ActionResult<User> Login([FromBody] LoginRequest loginRequest)
        {
            using (UnitOfWork unitOfWork = new UnitOfWork(_context))
            {
                User user = unitOfWork.Users
                    .Find(u => u.Username == loginRequest.Username && u.Password == loginRequest.Password)
                    .FirstOrDefault();

                if(user != null)
                {
                    return Ok(user);
                }
                else
                {
                    return NotFound();
                }
            }
        }

        [HttpGet]
        public ActionResult<TransactionsResponse> GetTransactions(Guid userId)
        {
            TransactionsResponse transactionsResponse = new TransactionsResponse
            {
                Purchases = new List<PurchaseBE>(),
                Tickets = new List<TicketBE>(),
                Vouchers = new List<VoucherBE>(),
                Orders = new List<OrderBE>()
            };

            using (UnitOfWork unitOfWork = new UnitOfWork(_context))
            {
                User user = unitOfWork.Users.GetWithRelated(userId);
                if (user == null)
                {
                    return NotFound("User");
                }

                user.Purchases.ForEach(p =>
                {
                    Purchase purchase = unitOfWork.Purchases.GetWithRelated(p.Id);

                    transactionsResponse.Purchases.Add(new PurchaseBE
                    {
                        Id = purchase.Id,
                        PerformanceId = purchase.PerformanceId,
                        PerformanceName = purchase.Performance.Name,
                        PerformanceDate = purchase.Performance.Date,
                        PaidValue = purchase.PaidValue,
                        TicketAmount = unitOfWork.Purchases.GetPurchaseTicketAmount(p.Id)
                    });

                    purchase.Tickets.ForEach(t =>
                    {
                        if(!t.Used)
                        {
                            transactionsResponse.Tickets.Add(new TicketBE
                            {
                                Id = t.Id,
                                PerformanceId = purchase.Performance.Id,
                                PerformanceName = purchase.Performance.Name,
                                PerformanceDate = purchase.Performance.Date,
                                PlaceInRoom = t.PlaceInRoom
                            });
                        }    
                    });
                });

                user.Vouchers.ForEach(v =>
                {
                    if(v.OrderId == null)
                    {
                        transactionsResponse.Vouchers.Add(new VoucherBE
                        {
                            Id = v.Id,
                            Type = v.Type
                        });
                    }
                });

                user.Orders.ForEach(o =>
                {
                    Order order = unitOfWork.Orders.GetWithRelated(o.Id);

                    List<string> acceptedVoucherTypes = new List<string>();
                    order.Vouchers.ForEach(v =>
                    {
                        acceptedVoucherTypes.Add(v.Type);
                    });

                    List<OrderProductBE> orderProducts = new List<OrderProductBE>();
                    order.OrdersProducts.ForEach(op =>
                    {
                        Product product = unitOfWork.Products.Get(op.ProductId);

                        orderProducts.Add(new OrderProductBE
                        {
                            Id = product.Id,
                            Name = product.Name,
                            Price = product.Price,
                            Quantity = op.Quantity
                        });
                    });

                    transactionsResponse.Orders.Add(new OrderBE
                    {
                        Id = o.Id,
                        PaidValue = o.PaidValue,
                        AcceptedVoucherTypes = acceptedVoucherTypes,
                        OrdersProducts = orderProducts
                    });
                });
            }

            return Ok(transactionsResponse);
        }
    }
}