import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProviderDetailComponent } from './provider-detail.component';

describe('Provider Management Detail Component', () => {
  let comp: ProviderDetailComponent;
  let fixture: ComponentFixture<ProviderDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProviderDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProviderDetailComponent,
              resolve: { provider: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProviderDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProviderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load provider on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProviderDetailComponent);

      // THEN
      expect(instance.provider()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
