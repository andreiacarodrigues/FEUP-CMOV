using CMOV1API.Model;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CMOV1API.Data
{
    public class ApplicationContext : DbContext
    {
        public ApplicationContext(DbContextOptions<ApplicationContext> options) : base(options) { }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<Performance>().HasData(
                new Performance { Id = 1, Name = "Dança com o Queirós", Date = new DateTime(2020, 12, 01, 18, 30, 0), Price = 20 },
                new Performance { Id = 2, Name = "Andreia no gelo", Date = new DateTime(2020, 12, 02, 20, 0, 0), Price = 40 },
                new Performance { Id = 3, Name = "Quebra-Nozes", Date = new DateTime(2020, 12, 03, 19, 00, 00), Price = 10 },
                new Performance { Id = 4, Name = "Concerto para salvar os pinguins", Date = new DateTime(2020, 12, 07, 19, 00, 00), Price = 10 },
                new Performance { Id = 5, Name = "Show de Ballet", Date = new DateTime(2020, 12, 09, 19, 00, 00), Price = 10 }
            );
            
            modelBuilder.Entity<Product>().HasData(
                new Product { Id = 1, Name = "Popcorn", Price = 3 },
                new Product { Id = 2, Name = "Coffee", Price = 0.25m },
                new Product { Id = 3, Name = "Soda", Price = 2 },
                new Product { Id = 4, Name = "Nachos", Price = 1},
                new Product { Id = 5, Name = "Water", Price = 1 },
                new Product { Id = 6, Name = "Candy", Price = 1 }
            );

            foreach (var relationship in modelBuilder.Model.GetEntityTypes().SelectMany(e => e.GetForeignKeys()))
            {
                relationship.DeleteBehavior = DeleteBehavior.Restrict;
            }

            modelBuilder.Entity<User>()
                .HasIndex(u => u.Username)
                .IsUnique();

            modelBuilder.Entity<OrderProduct>()
                .HasKey(op => new { op.OrderId, op.ProductId });
        }

        public DbSet<User> Users { get; set; }

        public DbSet<Performance> Performances { get; set; }

        public DbSet<Purchase> Purchases { get; set; }

        public DbSet<Ticket> Tickets { get; set; }

        public DbSet<Voucher> Vouchers { get; set; }

        public DbSet<Order> Orders { get; set; }

        public DbSet<Product> Products { get; set; }

        public DbSet<OrderProduct> OrdersProducts { get; set; }
    }
}