using CMOV1API.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Data.Repositories
{
    public class VoucherRepository : Repository<Voucher>
    {
        public VoucherRepository(ApplicationContext context) : base(context)
        {
            
        }

        public Voucher GetByGuid(Guid id)
        {
            return Context
                .Vouchers
                .Find(id);
        }
    }
}