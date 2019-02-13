using CMOV1API.Data;
using CMOV1API.Model;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Numerics;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace CMOV1API.Services
{
    public class EncryptionHelper
    {
        public EncryptionHelper()
        {
        }

        public static byte[] StringToByteArray(String hex)
        {
            int NumberChars = hex.Length;
            byte[] bytes = new byte[NumberChars / 2];
            for (int i = 0; i < NumberChars; i += 2)
                bytes[i / 2] = Convert.ToByte(hex.Substring(i, 2), 16);
            return bytes;
        }

        public bool IsValidKey(byte[] data, string signature, Guid userId, UnitOfWork unitOfWork)
        {
            User user = unitOfWork.Users.GetByGuid(userId);
            if (user == null)
            {
                return false;
            }

            using (RSA rsa = RSA.Create())
            {
                RSAParameters key = new RSAParameters
                {
                    Modulus = StringToByteArray(user.PublicKeyModulus),
                    Exponent = StringToByteArray(user.PublicKeyExponent)
                };
                rsa.ImportParameters(key);

                return rsa.VerifyData(data, Convert.FromBase64String(signature), HashAlgorithmName.SHA256, RSASignaturePadding.Pkcs1);
            }
        }
    }
}