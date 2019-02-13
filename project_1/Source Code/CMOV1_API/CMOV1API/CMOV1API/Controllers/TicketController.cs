using CMOV1API.Data;
using CMOV1API.Model;
using CMOV1API.Requests;
using CMOV1API.Responses;
using CMOV1API.Services;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Controllers
{
    [Produces("application/json")]
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class TicketController : ControllerBase
    {
        private readonly ApplicationContext _context;
        private readonly DatabaseErrorHandler _databaseErrorHandler;

        public TicketController(ApplicationContext context, DatabaseErrorHandler databaseErrorHandler)
        {
            _context = context;
            _databaseErrorHandler = databaseErrorHandler;
        }

        [HttpPost]
        public ActionResult<TicketsValidationResponse> ValidateTickets([FromBody] ValidateTicketsRequest validateTicketsRequest)
        {
            using (UnitOfWork unitOfWork = new UnitOfWork(_context))
            {
                User user = unitOfWork.Users.GetByGuid(validateTicketsRequest.UserId);
                if (user == null)
                {
                    return NotFound();
                }

                if(validateTicketsRequest.TicketIds.Count > 4)
                {
                    return BadRequest("Can only validate up to 4 tickets at once");
                }

                int performanceId = -1;
                List<Guid> validatedTickets = new List<Guid>();
                foreach (Guid ticketId in validateTicketsRequest.TicketIds)
                {
                    Ticket ticket = unitOfWork.Tickets.GetByGuid(ticketId);
                    if (ticket == null)
                    {
                        return BadRequest("Couldn't find ticket");
                    }

                    Purchase purchase = unitOfWork.Purchases.Get(ticket.PurchaseId);
                    if(purchase == null)
                    {
                        return BadRequest("Couldn't find purchase");
                    }

                    if(performanceId == -1)
                    {
                        performanceId = purchase.PerformanceId;
                    }
                    else if(performanceId != purchase.PerformanceId)
                    {
                        return BadRequest("Tickets are not all for the same performance");
                    }

                    if (purchase.UserId == validateTicketsRequest.UserId)
                    {
                        if(!ticket.Used)
                        {
                            ticket.Used = true;
                            validatedTickets.Add(ticketId);
                        }
                    }
                    else
                    {
                        return BadRequest("Not ticket owner");
                    }
                }

                try
                {
                    unitOfWork.Complete();
                }
                catch (Exception)
                {
                    return StatusCode(500);
                }

                TicketsValidationResponse ticketsValidationResponse = new TicketsValidationResponse
                {
                    TicketIds = validatedTickets
                };

                return Ok(ticketsValidationResponse);
            }
        }
    }
}