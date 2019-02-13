using CMOV1API.Business;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Responses
{
    public class TotalPriceAcceptedVouchersResponse
    {
        public int OrderId { get; set; }

        public List<VoucherBE> AcceptedVouchers { get; set; }

        public decimal TotalPrice { get; set; }
    }
}
