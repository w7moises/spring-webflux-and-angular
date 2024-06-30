import { Product } from './product';
export class Order {
  id: number;
  userId: string;
  productIds: number[];
  total: number;
  user: Object;
  products: Product[];
}
