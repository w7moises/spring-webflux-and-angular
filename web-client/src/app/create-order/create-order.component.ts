import { OrderService } from '../service/order.service';
import { Order } from '../models/order';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { UserService } from '../service/user.service';
import { Product } from '../models/product';
import { ProductService } from '../service/product.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-create-order',
  templateUrl: './create-order.component.html',
  styleUrls: ['./create-order.component.css']
})
export class CreateOrderComponent implements OnInit {

  order: Order = new Order();
  products: Product[] = [];
  selectedProductIds: number[] = [];
  user: any;
  userId: number;
  total: number = 0;
  orderForm: FormGroup;

  constructor(private orderService: OrderService, private formBuilder: FormBuilder, private snackBar: MatSnackBar,
    private userService: UserService, private productService: ProductService,
    private router: Router) { }

  ngOnInit() {
    this.loadData();
    this.orderForm = this.formBuilder.group({
      userId: ['', Validators.required],
      productIds: [[]],
      total: ['', Validators.required],
    });
  }

  loadData() {
    this.userId = parseInt(this.userService.getUserId());
    this.userService.getUserById(this.userId).subscribe(data => {
      this.user = data;
    });
    this.productService.getAllProducts().subscribe(data => {
      this.products = data;
    });
  }

  onSubmit() {
    this.orderForm.value.userId = this.userId;
    this.orderForm.value.productIds = this.selectedProductIds;
    this.orderForm.value.total = this.total;
    this.orderService.createOrder(this.orderForm.value)
      .subscribe(data => {
        this.snackBar.open('Orden actualizada exitosamente', 'Cerrar', {
          duration: 3000
        });
        setTimeout(() => {
          this.gotoList();
        }, 1000);
      });
  }

  gotoList() {
    this.router.navigate(['/order']);
  }

  onCheckboxChange(productId: number, isChecked: boolean) {
    if (isChecked) {
      this.selectedProductIds.push(productId);
      this.total += this.products.find(p => p.id === productId).price;
    } else {
      const index = this.selectedProductIds.indexOf(productId);
      if (index >= 0) {
        this.total -= this.products.find(p => p.id === productId).price;
        this.selectedProductIds.splice(index, 1);
      }
    }
  }

  formattedTotal() {
    return this.total.toFixed(2);
  }
  
}
