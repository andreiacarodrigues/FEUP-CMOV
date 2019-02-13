using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CMOV2_Xamarin
{

    public class MyMasterDetailPageMenuItem
    {
        public MyMasterDetailPageMenuItem()
        {
            TargetType = typeof(MainPage);
        }
        public int Id { get; set; }
        public string Title { get; set; }
        public string Font { get; set; }
        public string Logo { get; set; }

        public Type TargetType { get; set; }
    }
}