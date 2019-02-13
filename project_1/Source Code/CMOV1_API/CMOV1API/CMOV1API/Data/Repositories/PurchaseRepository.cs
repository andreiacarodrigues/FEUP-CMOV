using CMOV1API.Model;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Data.Repositories
{
    public class PurchaseRepository : Repository<Purchase>
    {
        public PurchaseRepository(ApplicationContext context) : base(context)
        {
            
        }

        public Purchase GetWithRelated(int purchaseId)
        {
            return Context
                .Purchases
                .Include(p => p.Performance)
                .Include(p => p.Tickets)
                .Where(p => p.Id == purchaseId)
                .FirstOrDefault();
        }

        public int GetPurchaseTicketAmount(int purchaseId)
        {
            Purchase purchase = GetWithRelated(purchaseId);
            return purchase.Tickets.Count();
        }
    }
}