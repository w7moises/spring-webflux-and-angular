import { Component, OnInit } from '@angular/core';
import { Order } from '../models/order';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../service/order.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Product } from '../models/product';
import { ProductService } from '../service/product.service';
import { UserService } from '../service/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-update-order',
  templateUrl: './update-order.component.html',
  styleUrls: ['./update-order.component.css']
})
export class UpdateOrderComponent implements OnInit {

  id: string;
  order: Order;
  products: Product[] = [];
  selectedProductIds: number[] = [];
  user: any;
  userId: number;
  total: number = 0;
  orderForm: FormGroup;

  constructor(private route: ActivatedRoute, private router: Router, private formBuilder: FormBuilder, private userService: UserService,
    private productService: ProductService, private snackBar: MatSnackBar,
    private orderService: OrderService) { }

  ngOnInit() {
    this.order = new Order();

    this.id = this.route.snapshot.params['id'];
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
    this.orderService.getOrder(this.id).subscribe(data => {
      this.order = data;
      this.total = this.order.total;
      this.selectedProductIds = this.order.productIds.slice();
    });
    this.productService.getAllProducts().subscribe(data => {
      this.products = data;
    });
  }

  onSubmit() {
    this.orderForm.value.userId = this.userId;
    this.orderForm.value.productIds = this.selectedProductIds;
    this.orderForm.value.total = this.total;
    this.orderService.updateOrder(this.id, this.orderForm.value).subscribe(data => {
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

  isSelected(productId: number): boolean {
    return this.selectedProductIds.includes(productId);
  }

  formattedTotal() {
    return this.total.toFixed(2);
  }
}
