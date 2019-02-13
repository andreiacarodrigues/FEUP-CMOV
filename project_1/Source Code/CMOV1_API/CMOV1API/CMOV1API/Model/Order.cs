using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Model
{
    public class Order
    {
        [Key]
        public int Id { get; set; }

        public Guid UserId { get; set; }

        [ForeignKey("UserId")]
        public User User { get; set; }

        [Column(TypeName = "Money")]
        public decimal PaidValue { get; set; }

        public List<Voucher> Vouchers { get; set; }

        public List<OrderProduct> OrdersProducts { get; set; }
    }
}