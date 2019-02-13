using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Model
{
    public class Voucher
    {
        [Key]
        public Guid Id { get; set; }

        public Guid UserId { get; set; }

        [ForeignKey("UserId")]
        public User User { get; set; }

        public int? OrderId { get; set; }

        [ForeignKey("OrderId")]
        public Order Order { get; set; }

        public string Type { get; set; }
    }
}