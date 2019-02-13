using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Requests
{
    public class ValidateTicketsRequest
    {
        public Guid UserId { get; set; }

        public List<Guid> TicketIds { get; set; }    
    }
}