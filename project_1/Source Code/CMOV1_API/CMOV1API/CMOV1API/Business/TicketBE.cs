using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Business
{
    public class TicketBE
    {
        public Guid Id { get; set; }

        public int PerformanceId { get; set; }

        public string PerformanceName { get; set; }

        public DateTime PerformanceDate { get; set; }

        public int PlaceInRoom { get; set; }
    }
}