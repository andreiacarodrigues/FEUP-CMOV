using CMOV1API.ValidationAttributes;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Requests
{
    public class MakePurchaseRequest
    {
        [Required]
        public Guid UserId { get; set; }

        [Required]
        public int PerformanceId { get; set; }

        [Required]
        [PosNumberNoZero(ErrorMessage = "Amount must be a positive integer")]
        public int NumberOfTickets { get; set; }

        [Required]
        public string Signature { get; set; }
    }
}