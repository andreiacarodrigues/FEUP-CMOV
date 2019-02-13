using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using CMOV1API.Data;
using CMOV1API.Model;
using CMOV1API.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace CMOV1API.Controllers
{
    [Produces("application/json")]
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class PerformanceController : ControllerBase
    {
        private readonly ApplicationContext _context;
        private readonly DatabaseErrorHandler _databaseErrorHandler;

        public PerformanceController(ApplicationContext context, DatabaseErrorHandler databaseErrorHandler)
        {
            _context = context;
            _databaseErrorHandler = databaseErrorHandler;
        }

        [HttpGet]
        public List<Performance> GetUpcomingPerformances()
        {
            using (UnitOfWork unitOfWork = new UnitOfWork(_context))
            {
                return unitOfWork.Performances.Find(p => p.Date >= DateTime.Now).ToList();
            }
        }
    }
}