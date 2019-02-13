using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.ValidationAttributes
{
    public class PosNumberNoZeroAttribute : ValidationAttribute
    {
        public override bool IsValid(object value)
        {
            if (value == null)
            {
                return true;
            }
            if (int.TryParse(value.ToString(), out int getal))
            {

                if (getal == 0)
                    return false;

                if (getal > 0)
                    return true;
            }
            return false;
        }
    }
}
