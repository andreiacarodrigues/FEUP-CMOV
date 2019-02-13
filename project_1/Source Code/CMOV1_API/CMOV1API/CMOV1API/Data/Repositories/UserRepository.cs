using CMOV1API.Model;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Data.Repositories
{
    public class UserRepository : Repository<User>
    {
        public UserRepository(ApplicationContext context) : base(context)
        {
            
        }

        public User GetByGuid(Guid id)
        {
            return Context
                .Users
                .Find(id);
        }

        public User GetWithRelated(Guid userId)
        {
            return Context
                .Users
                .Include(u => u.Purchases)
                .Include(u => u.Orders)
                .Include(u => u.Vouchers)
                .Where(u => u.Id == userId)
                .FirstOrDefault();
        }

        public decimal GetSpending(Guid userId)
        {
            decimal purchaseSpending = Context
                .Purchases
                .Where(p => p.UserId == userId)
                .Sum(p => p.PaidValue);

            decimal orderSpending = Context
                .Orders
                .Where(o => o.UserId == userId)
                .Sum(o => o.PaidValue);

            return purchaseSpending + orderSpending;
        }
    }
}