import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TechCVAltActivitiesDetailComponent } from './tech-cv-alt-activities-detail.component';

describe('TechCVAltActivities Management Detail Component', () => {
  let comp: TechCVAltActivitiesDetailComponent;
  let fixture: ComponentFixture<TechCVAltActivitiesDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechCVAltActivitiesDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TechCVAltActivitiesDetailComponent,
              resolve: { techCVAltActivities: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TechCVAltActivitiesDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TechCVAltActivitiesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load techCVAltActivities on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TechCVAltActivitiesDetailComponent);

      // THEN
      expect(instance.techCVAltActivities()).toEqual(expect.objectContaining({ id: 123 }));
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
