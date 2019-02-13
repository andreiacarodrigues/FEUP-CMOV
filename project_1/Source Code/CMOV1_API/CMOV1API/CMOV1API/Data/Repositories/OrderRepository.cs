using CMOV1API.Model;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Data.Repositories
{
    public class OrderRepository : Repository<Order>
    {
        public OrderRepository(ApplicationContext context) : base(context)
        {
            
        }

        public Order GetWithRelated(int orderId)
        {
            return Context
                .Orders
                .Include(o => o.Vouchers)
                .Include(o => o.OrdersProducts)
                .Where(o => o.Id == orderId)
                .FirstOrDefault();
        }
    }
}