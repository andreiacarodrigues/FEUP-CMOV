using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace CMOV1API.Migrations
{
    public partial class PerformancesNowHaveDetailedHoursOnInitialDataMigration : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.UpdateData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 1,
                column: "Date",
                value: new DateTime(2020, 12, 1, 18, 30, 0, 0, DateTimeKind.Unspecified));

            migrationBuilder.UpdateData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 2,
                column: "Date",
                value: new DateTime(2020, 12, 2, 20, 0, 0, 0, DateTimeKind.Unspecified));

            migrationBuilder.UpdateData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 3,
                column: "Date",
                value: new DateTime(2020, 12, 3, 19, 0, 0, 0, DateTimeKind.Unspecified));
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.UpdateData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 1,
                column: "Date",
                value: new DateTime(2020, 12, 1, 0, 0, 0, 0, DateTimeKind.Unspecified));

            migrationBuilder.UpdateData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 2,
                column: "Date",
                value: new DateTime(2020, 12, 2, 0, 0, 0, 0, DateTimeKind.Unspecified));

            migrationBuilder.UpdateData(
                table: "Performances",
                keyColumn: "Id",
                keyValue: 3,
                column: "Date",
                value: new DateTime(2020, 12, 3, 0, 0, 0, 0, DateTimeKind.Unspecified));
        }
    }
}
