using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Model
{
    public class User
    {
        [Key]
        public Guid Id { get; set; }

        [Required]
        public string Username { get; set; }

        [Required]
        public string Password { get; set; }

        [Required]
        public string Name { get; set; }

        [Required]
        public string NIF { get; set; }

        [Required]
        public string CreditCardNumber { get; set; }

        [Required]
        public string CreditCardType { get; set; }

        [Required]
        public string CreditCardExpiration { get; set; }

        [Required]
        public string PublicKeyModulus { get; set; }

        [Required]
        public string PublicKeyExponent { get; set; }

        public List<Purchase> Purchases { get; set; }

        public List<Order> Orders { get; set; }

        public List<Voucher> Vouchers { get; set; }
    }
}