import { Order } from '../models/order';
import { Component, OnInit, Input } from '@angular/core';
import { OrderService } from '../service/order.service';
import { OrderListComponent } from '../order-list/order-list.component';
import { Router, ActivatedRoute } from '@angular/router';
import { ProductService } from '../service/product.service';
import { Product } from '../models/product';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit {

  id: string;
  order: Order;
  products: Product[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private productService: ProductService,
    private orderService: OrderService) { }

  ngOnInit() {
    this.order = new Order();

    this.id = this.route.snapshot.params['id'];

    this.orderService.getOrder(this.id).subscribe(data => {
      this.order = data;
      data.productIds.forEach(productId => {
        this.productService.getProductById(productId).subscribe(product => {
          this.products.push(product);
        });
      });
    }, error => console.log(error));
  }

  list() {
    this.router.navigate(['order']);
  }
}
