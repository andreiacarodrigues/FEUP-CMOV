using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Business
{
    public class OrderBE
    {
        public int Id { get; set; }

        public decimal PaidValue { get; set; }

        public List<string> AcceptedVoucherTypes { get; set; }

        public List<OrderProductBE> OrdersProducts { get; set; }
    }
}