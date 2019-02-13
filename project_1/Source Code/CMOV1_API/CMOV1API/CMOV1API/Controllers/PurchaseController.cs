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
    public class PurchaseController : ControllerBase
    {
        private const int BONUS_MULTIPLIER = 100;
        private readonly ApplicationContext _context;
        private readonly DatabaseErrorHandler _databaseErrorHandler;
        private readonly EncryptionHelper _encryptionHelper;

        public PurchaseController(ApplicationContext context, DatabaseErrorHandler databaseErrorHandler, EncryptionHelper encryptionHelper)
        {
            _context = context;
            _databaseErrorHandler = databaseErrorHandler;
            _encryptionHelper = encryptionHelper;
        }

        [HttpPost]
        public ActionResult<TicketsAndVouchersResponse> MakePurchase([FromBody] MakePurchaseRequest makePurchaseRequest)
        {
            byte[] data;
            using (MemoryStream memoryStream = new MemoryStream())
            {
                BinaryWriter binaryWriter = new BinaryWriter(memoryStream);

                using (MemoryStream memoryStream2 = new MemoryStream())
                {
                    BinaryWriter binaryWriter2 = new BinaryWriter(memoryStream2);
                    binaryWriter2.Write(makePurchaseRequest.PerformanceId);
                    byte[] array2 = memoryStream2.ToArray();
                    Array.Reverse(array2);
                    binaryWriter.Write(array2);
                }

                using (MemoryStream memStream3 = new MemoryStream())
                {
                    BinaryWriter binaryWriter3 = new BinaryWriter(memStream3);
                    binaryWriter3.Write(makePurchaseRequest.NumberOfTickets);
                    byte[] array3 = memStream3.ToArray();
                    Array.Reverse(array3);
                    binaryWriter.Write(array3);
                }
                    
                String userId = makePurchaseRequest.UserId.ToString();
                binaryWriter.Write(userId.ToCharArray());
                data = memoryStream.ToArray();
            }

            using (UnitOfWork unitOfWork = new UnitOfWork(_context))        
            {
                if(!_encryptionHelper.IsValidKey(data, makePurchaseRequest.Signature, makePurchaseRequest.UserId, unitOfWork))
                {
                    return Forbid("Invalid Signature");
                }

                // User
                User user = unitOfWork.Users.GetByGuid(makePurchaseRequest.UserId);
                if (user == null)
                {
                    return NotFound("User");
                }

                // Performance
                Performance performance = unitOfWork.Performances.Get(makePurchaseRequest.PerformanceId);
                if (performance == null)
                {
                    return NotFound("Performance");
                }

                // Purchase
                Purchase purchase = new Purchase
                {
                    User = user,
                    Performance = performance,
                    PaidValue = performance.Price * makePurchaseRequest.NumberOfTickets
                };

                unitOfWork.Purchases.Add(purchase);

                // Tickets
                Random ticketsRandom = new Random();
                int max = 101 - makePurchaseRequest.NumberOfTickets;
                max = (int)MathF.Max(max, 2);
                int seat = ticketsRandom.Next(1, max);

                for(int i = 0; i < makePurchaseRequest.NumberOfTickets; i++, seat++)
                {
                    Ticket ticket = new Ticket
                    {
                        Purchase = purchase,
                        PlaceInRoom = seat,
                        Used = false
                    };

                    unitOfWork.Tickets.Add(ticket);
                }

                // Vouchers
                Random vouchersRandom = new Random();
                for (int i = 0; i < makePurchaseRequest.NumberOfTickets; i++, seat++)
                {
                    int random = vouchersRandom.Next(2);

                    Voucher voucher = new Voucher
                    {
                        User = user,
                        Type = random == 1 ? "FreeCoffee" : "FreePopcorn"
                    };

                    unitOfWork.Vouchers.Add(voucher);
                }

                // Bonus Vouchers
                decimal currentSpending = unitOfWork.Users.GetSpending(makePurchaseRequest.UserId);
                int currentSpendingMultiplier = (int)currentSpending / BONUS_MULTIPLIER;

                decimal newSpending = currentSpending + purchase.PaidValue;
                int newSpendingMultiplier = (int)newSpending / BONUS_MULTIPLIER;

                int differenceMultiplier = newSpendingMultiplier - currentSpendingMultiplier;
                for(int i = 0; i < differenceMultiplier; i++)
                {
                    Voucher voucher = new Voucher
                    {
                        User = user,
                        Type = "5Cafeteria"
                    };

                    unitOfWork.Vouchers.Add(voucher);
                }

                // Saving
                try
                {
                    unitOfWork.Complete();
                }
                catch (Exception)
                {
                    return StatusCode(500);
                }

                // Response
                TicketsAndVouchersResponse response = new TicketsAndVouchersResponse
                {
                    Tickets = new List<TicketBE>(),
                    Vouchers = new List<VoucherBE>()
                };

                purchase.Tickets.ForEach(t =>
                {
                    response.Tickets.Add(new TicketBE
                    {
                        Id = t.Id,
                        PerformanceId = performance.Id,
                        PerformanceName = performance.Name,
                        PerformanceDate = performance.Date,
                        PlaceInRoom = t.PlaceInRoom
                    });
                });

                user.Vouchers.ForEach(v =>
                {
                    response.Vouchers.Add(new VoucherBE
                    {
                        Id = v.Id,
                        Type = v.Type
                    });
                });

                return response;
            }
        }
    }
}