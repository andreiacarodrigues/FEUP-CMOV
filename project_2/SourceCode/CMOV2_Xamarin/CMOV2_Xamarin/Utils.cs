using System;
using System.Collections.Generic;
using System.Text;
using Xamarin.Forms;

namespace CMOV2_Xamarin
{
    /* "Apple, IBM, Hewlett Packard, Microsoft, Oracle, Google, Facebook, Twitter, Intel, AMD" */

    static class Utils
    {
        public static string API_KEY = "504b1dfd86c5a70407599643008d16fb";
        public static string APPLE = "AAPL";
        public static string IBM = "IBM";
        public static string HEWLETT = "HPE";
        public static string MICROSOFT = "MSFT";
        public static string ORACLE = "ORCL";
        public static string GOOGLE = "GOOGL";
        public static string FACEBOOK = "FB";
        public static string TWITTER = "TWTR";
        public static string INTEL = "INTC";
        public static string AMD = "AMD";

        public static List<Company> CompaniesList = new List<Company>
            {
                new Company() { Name = "Apple", Tick = APPLE,
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                    Logo = APPLE + ".png"
                },
                new Company() { Name = "IBM", Tick = IBM,
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                    Logo = IBM + ".png"
                },
                new Company() { Name = "Hewlett Packard", Tick = HEWLETT,
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                    Logo = HEWLETT + ".png"
                },
                new Company() { Name = "Microsoft", Tick = MICROSOFT,
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                    Logo = MICROSOFT + ".png"
                },
                new Company() { Name = "Oracle", Tick = ORACLE,
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                    Logo = ORACLE + ".png"
                },
                new Company() { Name = "Google", Tick = GOOGLE,
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                    Logo = GOOGLE + ".png"
                },
                new Company() { Name = "Facebook", Tick = FACEBOOK,
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                    Logo = FACEBOOK + ".png"
                },
                new Company() { Name = "Twitter", Tick = TWITTER,
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                    Logo = TWITTER + ".png"
                },
                new Company() { Name = "Intel", Tick = INTEL,
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                    Logo = INTEL + ".png"
                },
                new Company() { Name = "AMD", Tick = AMD,
                    Font = Device.RuntimePlatform == Device.Android ? "Ubuntu-Bold.ttf#Ubuntu-Bold" :
                        Device.RuntimePlatform == Device.UWP ? "Assets/Ubuntu-Bold.ttf#Ubuntu" : "Assets/Ubuntu-Bold.ttf#Ubuntu",
                    Logo = AMD + ".png"
                },
    };

        public static string GetTick(string name) {

            for (int i = 0; i < CompaniesList.Count; i++)
            {
                if (CompaniesList[i].Name.Equals(name))
                    return CompaniesList[i].Tick;
            }
            return null;
        }

        public static Company GetCompany(string name)
        {

            for (int i = 0; i < CompaniesList.Count; i++)
            {
                if (CompaniesList[i].Name.Equals(name))
                    return CompaniesList[i];
            }
            return null;
        }

        public static List<Company> GetCompanies()
        {
            var companies = new List<Company>()
            {
                new Company(){ Name = "Apple", Tick = APPLE },
                new Company(){ Name = "IBM", Tick = IBM },
                new Company(){ Name = "Hewlett Packard", Tick = HEWLETT },
                new Company(){ Name = "Microsoft", Tick = MICROSOFT },
                new Company(){ Name = "Oracle", Tick = ORACLE },
                new Company(){ Name = "Google", Tick = GOOGLE },
                new Company(){ Name = "Facebook", Tick = FACEBOOK },
                new Company(){ Name = "Twitter", Tick = TWITTER },
                new Company(){ Name = "Intel", Tick = INTEL },
                new Company(){ Name = "AMD", Tick = AMD }
            };

            return companies;
        }
    }
}
