using CMOV1API.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Data.Repositories
{
    public class PerformanceRepository : Repository<Performance>
    {
        public PerformanceRepository(ApplicationContext context) : base(context)
        {
            
        }
    }
}