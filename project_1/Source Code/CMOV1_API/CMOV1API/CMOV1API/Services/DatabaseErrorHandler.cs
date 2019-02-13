using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Services
{
    public class DatabaseErrorHandler
    {
        public string DbUpdateExceptionHandler(DbUpdateException e)
        {
            Exception innerException = e.InnerException;
            if (innerException != null && innerException is SqlException)
            {
                SqlException sqlException = (SqlException)innerException;

                switch (sqlException.Number)
                {
                    case 2627:
                        return "Unique constraint error";

                    case 547:
                        return "Constraint check violation";

                    case 2601:
                        return "Duplicated key row error";

                    default:
                        return "";
                }
            }
            return "";
        }
    }
}