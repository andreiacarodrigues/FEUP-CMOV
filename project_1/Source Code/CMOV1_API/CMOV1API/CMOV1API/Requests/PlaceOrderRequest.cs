using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Requests
{
    public class PlaceOrderRequest
    {
        [Required]
        public Guid UserId { get; set; }

        public List<Guid> VoucherIds { get; set; }

        [Required]
        public List<int> ProductIds { get; set; }

        [Required]
        public string Signature { get; set; }
    }
}