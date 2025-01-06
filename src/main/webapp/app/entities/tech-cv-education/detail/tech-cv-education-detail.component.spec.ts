import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TechCVEducationDetailComponent } from './tech-cv-education-detail.component';

describe('TechCVEducation Management Detail Component', () => {
  let comp: TechCVEducationDetailComponent;
  let fixture: ComponentFixture<TechCVEducationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechCVEducationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TechCVEducationDetailComponent,
              resolve: { techCVEducation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TechCVEducationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TechCVEducationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load techCVEducation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TechCVEducationDetailComponent);

      // THEN
      expect(instance.techCVEducation()).toEqual(expect.objectContaining({ id: 123 }));
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
