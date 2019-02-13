﻿// <auto-generated />
using System;
using CMOV1API.Data;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;
using Microsoft.EntityFrameworkCore.Storage.ValueConversion;

namespace CMOV1API.Migrations
{
    [DbContext(typeof(ApplicationContext))]
    [Migration("20181110152353_PerformancesNowHaveDetailedHoursOnInitialDataMigration")]
    partial class PerformancesNowHaveDetailedHoursOnInitialDataMigration
    {
        protected override void BuildTargetModel(ModelBuilder modelBuilder)
        {
#pragma warning disable 612, 618
            modelBuilder
                .HasAnnotation("ProductVersion", "2.1.1-rtm-30846")
                .HasAnnotation("Relational:MaxIdentifierLength", 128)
                .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

            modelBuilder.Entity("CMOV1API.Model.Order", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<decimal>("PaidValue")
                        .HasColumnType("Money");

                    b.Property<Guid>("UserId");

                    b.HasKey("Id");

                    b.HasIndex("UserId");

                    b.ToTable("Orders");
                });

            modelBuilder.Entity("CMOV1API.Model.OrderProduct", b =>
                {
                    b.Property<int>("OrderId");

                    b.Property<int>("ProductId");

                    b.Property<int>("Quantity");

                    b.HasKey("OrderId", "ProductId");

                    b.HasIndex("ProductId");

                    b.ToTable("OrdersProducts");
                });

            modelBuilder.Entity("CMOV1API.Model.Performance", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<DateTime>("Date");

                    b.Property<string>("Name")
                        .IsRequired();

                    b.Property<decimal>("Price")
                        .HasColumnType("Money");

                    b.HasKey("Id");

                    b.ToTable("Performances");

                    b.HasData(
                        new { Id = 1, Date = new DateTime(2020, 12, 1, 18, 30, 0, 0, DateTimeKind.Unspecified), Name = "Dança com o Queirós", Price = 20m },
                        new { Id = 2, Date = new DateTime(2020, 12, 2, 20, 0, 0, 0, DateTimeKind.Unspecified), Name = "Patinagem com a Andreia", Price = 40m },
                        new { Id = 3, Date = new DateTime(2020, 12, 3, 19, 0, 0, 0, DateTimeKind.Unspecified), Name = "Eu Nuno", Price = 10m }
                    );
                });

            modelBuilder.Entity("CMOV1API.Model.Product", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<string>("Name");

                    b.Property<decimal>("Price");

                    b.HasKey("Id");

                    b.ToTable("Products");

                    b.HasData(
                        new { Id = 1, Name = "Popcorn", Price = 3m },
                        new { Id = 2, Name = "Coffee", Price = 0.25m },
                        new { Id = 3, Name = "Soda", Price = 2m },
                        new { Id = 4, Name = "Nachos", Price = 1m }
                    );
                });

            modelBuilder.Entity("CMOV1API.Model.Purchase", b =>
                {
                    b.Property<int>("Id")
                        .ValueGeneratedOnAdd()
                        .HasAnnotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn);

                    b.Property<decimal>("PaidValue")
                        .HasColumnType("Money");

                    b.Property<int>("PerformanceId");

                    b.Property<Guid>("UserId");

                    b.HasKey("Id");

                    b.HasIndex("PerformanceId");

                    b.HasIndex("UserId");

                    b.ToTable("Purchases");
                });

            modelBuilder.Entity("CMOV1API.Model.Ticket", b =>
                {
                    b.Property<Guid>("Id")
                        .ValueGeneratedOnAdd();

                    b.Property<int>("PlaceInRoom");

                    b.Property<int>("PurchaseId");

                    b.Property<bool>("Used");

                    b.HasKey("Id");

                    b.HasIndex("PurchaseId");

                    b.ToTable("Tickets");
                });

            modelBuilder.Entity("CMOV1API.Model.User", b =>
                {
                    b.Property<Guid>("Id")
                        .ValueGeneratedOnAdd();

                    b.Property<string>("CreditCardExpiration")
                        .IsRequired();

                    b.Property<string>("CreditCardNumber")
                        .IsRequired();

                    b.Property<string>("CreditCardType")
                        .IsRequired();

                    b.Property<string>("NIF")
                        .IsRequired();

                    b.Property<string>("Name")
                        .IsRequired();

                    b.Property<string>("Password")
                        .IsRequired();

                    b.Property<string>("PublicKey")
                        .IsRequired();

                    b.Property<string>("Username")
                        .IsRequired();

                    b.HasKey("Id");

                    b.HasIndex("Username")
                        .IsUnique();

                    b.ToTable("Users");
                });

            modelBuilder.Entity("CMOV1API.Model.Voucher", b =>
                {
                    b.Property<Guid>("Id")
                        .ValueGeneratedOnAdd();

                    b.Property<int?>("OrderId");

                    b.Property<string>("Type");

                    b.Property<Guid>("UserId");

                    b.HasKey("Id");

                    b.HasIndex("OrderId");

                    b.HasIndex("UserId");

                    b.ToTable("Vouchers");
                });

            modelBuilder.Entity("CMOV1API.Model.Order", b =>
                {
                    b.HasOne("CMOV1API.Model.User", "User")
                        .WithMany("Orders")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Restrict);
                });

            modelBuilder.Entity("CMOV1API.Model.OrderProduct", b =>
                {
                    b.HasOne("CMOV1API.Model.Order", "Order")
                        .WithMany("OrdersProducts")
                        .HasForeignKey("OrderId")
                        .OnDelete(DeleteBehavior.Restrict);

                    b.HasOne("CMOV1API.Model.Product", "Product")
                        .WithMany()
                        .HasForeignKey("ProductId")
                        .OnDelete(DeleteBehavior.Restrict);
                });

            modelBuilder.Entity("CMOV1API.Model.Purchase", b =>
                {
                    b.HasOne("CMOV1API.Model.Performance", "Performance")
                        .WithMany()
                        .HasForeignKey("PerformanceId")
                        .OnDelete(DeleteBehavior.Restrict);

                    b.HasOne("CMOV1API.Model.User", "User")
                        .WithMany("Purchases")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Restrict);
                });

            modelBuilder.Entity("CMOV1API.Model.Ticket", b =>
                {
                    b.HasOne("CMOV1API.Model.Purchase", "Purchase")
                        .WithMany("Tickets")
                        .HasForeignKey("PurchaseId")
                        .OnDelete(DeleteBehavior.Restrict);
                });

            modelBuilder.Entity("CMOV1API.Model.Voucher", b =>
                {
                    b.HasOne("CMOV1API.Model.Order", "Order")
                        .WithMany("Vouchers")
                        .HasForeignKey("OrderId")
                        .OnDelete(DeleteBehavior.Restrict);

                    b.HasOne("CMOV1API.Model.User", "User")
                        .WithMany("Vouchers")
                        .HasForeignKey("UserId")
                        .OnDelete(DeleteBehavior.Restrict);
                });
#pragma warning restore 612, 618
        }
    }
}