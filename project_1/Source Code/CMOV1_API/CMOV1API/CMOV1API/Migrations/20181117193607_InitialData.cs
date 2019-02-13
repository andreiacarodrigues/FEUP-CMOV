using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace CMOV1API.Migrations
{
    public partial class InitialData : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.UpdateData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 2,
                column: "Name",
                value: "Andreia no gelo");

            migrationBuilder.UpdateData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 3,
                column: "Name",
                value: "Quebra-Nozes");

            migrationBuilder.InsertData(
                table: "Performances",
                columns: new[] { "Id", "Date", "Name", "Price" },
                values: new object[,]
                {
                    { 4, new DateTime(2020, 12, 7, 19, 0, 0, 0, DateTimeKind.Unspecified), "Concerto para salvar os pinguins", 10m },
                    { 5, new DateTime(2020, 12, 9, 19, 0, 0, 0, DateTimeKind.Unspecified), "Show de Ballet", 10m }
                });

            migrationBuilder.InsertData(
                table: "Products",
                columns: new[] { "Id", "Name", "Price" },
                values: new object[,]
                {
                    { 5, "Water", 1m },
                    { 6, "Candy", 1m }
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DeleteData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 4);

            migrationBuilder.DeleteData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 5);

            migrationBuilder.DeleteData(
                table: "Products",
                keyColumn: "Id",
                keyValue: 5);

            migrationBuilder.DeleteData(
                table: "Products",
                keyColumn: "Id",
                keyValue: 6);

            migrationBuilder.UpdateData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 2,
                column: "Name",
                value: "Patinagem com a Andreia");

            migrationBuilder.UpdateData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 3,
                column: "Name",
                value: "Eu Nuno");
        }
    }
}
