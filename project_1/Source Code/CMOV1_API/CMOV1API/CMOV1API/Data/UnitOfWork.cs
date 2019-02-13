using CMOV1API.Data.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Data
{
    public class UnitOfWork : IDisposable
    {
        private readonly ApplicationContext _context;

        public UnitOfWork(ApplicationContext context)
        {
            _context = context;
            Users = new UserRepository(context);
            Performances = new PerformanceRepository(context);
            Purchases = new PurchaseRepository(context);
            Tickets = new TicketRepository(context);
            Vouchers = new VoucherRepository(context);
            Orders = new OrderRepository(context);
            Products = new ProductRepository(context);
            OrdersProducts = new OrderProductRepository(context);
        }

        public UserRepository Users { get; private set; }

        public PerformanceRepository Performances { get; private set; }

        public PurchaseRepository Purchases { get; private set; }

        public TicketRepository Tickets { get; private set; }

        public VoucherRepository Vouchers { get; private set; }

        public OrderRepository Orders { get; private set; }
        
        public ProductRepository Products { get; private set; }

        public OrderProductRepository OrdersProducts { get; private set; }

        public int Complete()
        {
            return _context.SaveChanges();
        }

        public void Dispose()
        {
            _context.Dispose();
        }
    }
}