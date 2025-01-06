import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TechCVEmploymentDetailComponent } from './tech-cv-employment-detail.component';

describe('TechCVEmployment Management Detail Component', () => {
  let comp: TechCVEmploymentDetailComponent;
  let fixture: ComponentFixture<TechCVEmploymentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechCVEmploymentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TechCVEmploymentDetailComponent,
              resolve: { techCVEmployment: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TechCVEmploymentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TechCVEmploymentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load techCVEmployment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TechCVEmploymentDetailComponent);

      // THEN
      expect(instance.techCVEmployment()).toEqual(expect.objectContaining({ id: 123 }));
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
