import { Directive, ElementRef, HostListener, AfterViewInit } from '@angular/core';

@Directive({
    selector: '[appDynamicTooltip]'
})
export class DynamicTooltipDirective implements AfterViewInit {

    private isTooltipShown: boolean = false;

    constructor(private elementRef: ElementRef) { }

    ngAfterViewInit() {
        this.observeContentChanges();
    }

    @HostListener('mouseenter') onMouseEnter() {
        this.showTooltip();
    }

    @HostListener('mouseleave') onMouseLeave() {
        this.hideTooltip();
    }

    private observeContentChanges() {
        const element = this.elementRef.nativeElement;
        const observer = new MutationObserver(() => {
            this.checkTooltipVisibility();
        });

        observer.observe(element, { attributes: true, childList: true, subtree: true });
    }

    private checkTooltipVisibility() {
        const element = this.elementRef.nativeElement;
        if (this.isContentOverflowing(element) && !this.isTooltipShown) {
            this.showTooltip();
        } else {
            this.hideTooltip();
        }
    }

    private isContentOverflowing(element: HTMLElement): boolean {
        return element.scrollWidth > element.clientWidth || element.scrollHeight > element.clientHeight;
    }

    private showTooltip() {
        const nativeElement = this.elementRef.nativeElement;
        if (!this.isTooltipShown) {
            nativeElement.setAttribute('title', nativeElement.innerText);
            this.isTooltipShown = true;
        }
    }

    private hideTooltip() {
        const nativeElement = this.elementRef.nativeElement;
        if (this.isTooltipShown) {
            nativeElement.removeAttribute('title');
            this.isTooltipShown = false;
        }
    }
}
