using CMOV1API.Business;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Responses
{
    public class TicketsAndVouchersResponse
    {
        public List<TicketBE> Tickets { get; set; }

        public List<VoucherBE> Vouchers { get; set; }
    }
}