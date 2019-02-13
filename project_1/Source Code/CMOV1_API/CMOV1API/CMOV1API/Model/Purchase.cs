using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Model
{
    public class Purchase
    {
        [Key]
        public int Id { get; set; }

        public Guid UserId { get; set; }

        [ForeignKey("UserId")]
        public User User { get; set; }

        public int PerformanceId { get; set; }

        [ForeignKey("PerformanceId")]
        public Performance Performance { get; set; }

        [Column(TypeName = "Money")]
        public decimal PaidValue { get; set; }

        public List<Ticket> Tickets { get; set; }
    }
}