import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TechCVAchievementDetailComponent } from './tech-cv-achievement-detail.component';

describe('TechCVAchievement Management Detail Component', () => {
  let comp: TechCVAchievementDetailComponent;
  let fixture: ComponentFixture<TechCVAchievementDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechCVAchievementDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TechCVAchievementDetailComponent,
              resolve: { techCVAchievement: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TechCVAchievementDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TechCVAchievementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load techCVAchievement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TechCVAchievementDetailComponent);

      // THEN
      expect(instance.techCVAchievement()).toEqual(expect.objectContaining({ id: 123 }));
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
