using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Model
{
    public class Ticket
    {
        [Key]
        public Guid Id { get; set; }

        public int PurchaseId { get; set; }

        [ForeignKey("PurchaseId")]
        public Purchase Purchase { get; set; }

        public int PlaceInRoom { get; set; }

        public bool Used { get; set; }
    }
}