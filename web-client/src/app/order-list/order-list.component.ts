import { OrderDetailsComponent } from '../order-details/order-details.component';
import { Observable, forkJoin } from "rxjs";
import { OrderService } from "../service/order.service";
import { Order } from "../models/order";
import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { ProductService } from '../service/product.service';
import { Product } from '../models/product';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: "app-order-list",
  templateUrl: "./order-list.component.html",
  styleUrls: ["./order-list.component.css"]
})
export class OrderListComponent implements OnInit {
  orders: Order[] = [];
  user: any;

  constructor(private orderService: OrderService, private userService: UserService, private productService: ProductService, private snackBar: MatSnackBar,
    private router: Router) { }

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    let userId = this.userService.getUserId();
    this.userService.getUserById(parseInt(userId)).subscribe(data => {
      this.user = data;
    });
    this.orderService.getOrderByUserId(parseInt(userId)).subscribe(data => {
      this.orders = data;
      this.loadProductsForOrders();
    });
  }

  loadProductsForOrders() {
    for (let order of this.orders) {
      let productObservables: Observable<Product>[] = [];
      if (order.productIds != null) {
        for (let productId of order.productIds) {
          productObservables.push(this.productService.getProductById(productId));
        }

        forkJoin(productObservables).subscribe(products => {
          order.products = products;
        });
      } else {
        order.products = [];
      }
    }
  }

  deleteOrder(id: number) {
    this.orderService.deleteOrder(id).subscribe(data => {
      this.reloadData();
      this.snackBar.open('Orden eliminada exitosamente', 'Cerrar', {
        duration: 3000
      });
      setTimeout(() => {
        this.router.navigate(['/order']);
      }, 2000);
    });
  }

  orderDetails(id: number) {
    this.router.navigate(['details', id]);
  }

  updateOrder(id: number) {
    this.router.navigate(['update', id]);
  }

  createOrder() {
    this.router.navigate(['add']);
  }
}
