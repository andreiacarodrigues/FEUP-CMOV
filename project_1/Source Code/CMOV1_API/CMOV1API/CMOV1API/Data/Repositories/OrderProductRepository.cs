using CMOV1API.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Data.Repositories
{
    public class OrderProductRepository : Repository<OrderProduct>
    {
        public OrderProductRepository(ApplicationContext context) : base(context)
        {
            
        }
    }
}