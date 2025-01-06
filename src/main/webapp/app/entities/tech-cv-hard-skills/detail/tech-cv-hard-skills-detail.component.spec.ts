import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TechCVHardSkillsDetailComponent } from './tech-cv-hard-skills-detail.component';

describe('TechCVHardSkills Management Detail Component', () => {
  let comp: TechCVHardSkillsDetailComponent;
  let fixture: ComponentFixture<TechCVHardSkillsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechCVHardSkillsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TechCVHardSkillsDetailComponent,
              resolve: { techCVHardSkills: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TechCVHardSkillsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TechCVHardSkillsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load techCVHardSkills on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TechCVHardSkillsDetailComponent);

      // THEN
      expect(instance.techCVHardSkills()).toEqual(expect.objectContaining({ id: 123 }));
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
