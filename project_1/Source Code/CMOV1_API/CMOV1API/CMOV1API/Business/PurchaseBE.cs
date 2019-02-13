using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Business
{
    public class PurchaseBE
    {
        public int Id { get; set; }

        public int PerformanceId { get; set; }

        public string PerformanceName { get; set; }

        public DateTime PerformanceDate { get; set; }

        public decimal PaidValue { get; set; }

        public int TicketAmount { get; set; }
    }
}