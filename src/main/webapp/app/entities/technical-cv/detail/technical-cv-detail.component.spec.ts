import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TechnicalCVDetailComponent } from './technical-cv-detail.component';

describe('TechnicalCV Management Detail Component', () => {
  let comp: TechnicalCVDetailComponent;
  let fixture: ComponentFixture<TechnicalCVDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechnicalCVDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TechnicalCVDetailComponent,
              resolve: { technicalCV: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TechnicalCVDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TechnicalCVDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load technicalCV on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TechnicalCVDetailComponent);

      // THEN
      expect(instance.technicalCV()).toEqual(expect.objectContaining({ id: 123 }));
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
