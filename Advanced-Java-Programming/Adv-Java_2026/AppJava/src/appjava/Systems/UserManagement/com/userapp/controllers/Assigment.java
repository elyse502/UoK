package appjava.Systems.UserManagement.com.userapp.controllers;
class Product
{
  double prodID;  
  String prodName;
  String prodDesc;
  int prodQuantity;

void Displayinfo()
{
System.out.println("product id :"+prodID);
System.out.println("product id :"+prodName);
System.out.println("product id :"+prodDesc);
System.out.println("product id :"+prodQuantity);
}
public Product(double prodID,String prodName,String prodDesc,int prodQuantity)
{
    this.prodID = prodID;
    this.prodName = prodName;
    this.prodDesc = prodDesc;
    this.prodQuantity = prodQuantity;
}
}
public class Assigment {
    public static void main(String[] args) {
        Product p = new Product();
        p.prodID=3;
        p.prodName="Biscuit";
        p.prodDesc="Less suger";
        p.prodQuantity=5;
        System.out.println("The product id is"+p.prodID);
        System.out.println("The product name is"+p.prodName);
        System.out.println("The product description is"+p.prodDesc);
        System.out.println("The product Quantity is"+p.prodQuantity);
        
    }
    
}
