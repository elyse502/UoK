import 'package:flutter/material.dart';

class CheckoutPage extends StatefulWidget {
  const CheckoutPage({super.key});

  @override
  State<CheckoutPage> createState() => _CheckoutPageState();
}

class _CheckoutPageState extends State<CheckoutPage> {
  final _formKey = GlobalKey<FormState>();
  bool useDifferentBilling = false;
  String? imagePath;

  final TextEditingController fullNameController = TextEditingController();
  final TextEditingController addressController = TextEditingController();
  final TextEditingController phoneController = TextEditingController();
  final TextEditingController billingController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Checkout")),
      body: Padding(
        padding: EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: fullNameController,
                validator: (value) =>
                    (value == null || value.split(' ').length < 2)
                    ? 'Enter full name (at least two words)'
                    : null,
                decoration: InputDecoration(labelText: "Full Name"),
              ),
              TextFormField(
                controller: addressController,
                validator: (value) => (value == null || value.length < 15)
                    ? 'Address must be at least 15 characters'
                    : null,
                decoration: InputDecoration(labelText: "Shipping Address"),
              ),
              TextFormField(
                controller: phoneController,
                validator: (value) =>
                    (value == null || !RegExp(r'^[0-9+()\-]+').hasMatch(value))
                    ? 'Invalid phone number'
                    : null,
                decoration: InputDecoration(labelText: "Phone Number"),
              ),
              CheckboxListTile(
                title: Text("Use a different billing address"),
                value: useDifferentBilling,
                onChanged: (val) => setState(() => useDifferentBilling = val!),
              ),
              if (useDifferentBilling)
                TextFormField(
                  controller: billingController,
                  validator: (val) => val == null || val.isEmpty
                      ? "Billing address required"
                      : null,
                  decoration: InputDecoration(labelText: "Billing Address"),
                ),
              SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  if (_formKey.currentState!.validate()) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text("Order placed successfully!")),
                    );
                  }
                },
                child: Text("Place Order"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
