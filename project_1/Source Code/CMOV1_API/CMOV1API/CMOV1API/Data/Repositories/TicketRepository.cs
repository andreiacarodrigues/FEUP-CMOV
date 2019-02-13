using CMOV1API.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Data.Repositories
{
    public class TicketRepository : Repository<Ticket>
    {
        public TicketRepository(ApplicationContext context) : base(context)
        {
            
        }

        public Ticket GetByGuid(Guid id)
        {
            return Context
                .Tickets
                .Find(id);
        }
    }
}