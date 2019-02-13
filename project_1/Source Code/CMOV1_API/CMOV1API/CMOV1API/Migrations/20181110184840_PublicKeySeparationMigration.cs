using Microsoft.EntityFrameworkCore.Migrations;

namespace CMOV1API.Migrations
{
    public partial class PublicKeySeparationMigration : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "PublicKey",
                table: "Users",
                newName: "PublicKeyModulus");

            migrationBuilder.AddColumn<string>(
                name: "PublicKeyExponent",
                table: "Users",
                nullable: false,
                defaultValue: "");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "PublicKeyExponent",
                table: "Users");

            migrationBuilder.RenameColumn(
                name: "PublicKeyModulus",
                table: "Users",
                newName: "PublicKey");
        }
    }
}
