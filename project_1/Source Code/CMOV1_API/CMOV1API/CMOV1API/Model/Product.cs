using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Model
{
    public class Product
    {
        [Key]
        public int Id { get; set; }

        public string Name { get; set; }

        public decimal Price { get; set; }
    }
}
