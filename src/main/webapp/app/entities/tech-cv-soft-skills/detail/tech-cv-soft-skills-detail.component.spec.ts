import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TechCVSoftSkillsDetailComponent } from './tech-cv-soft-skills-detail.component';

describe('TechCVSoftSkills Management Detail Component', () => {
  let comp: TechCVSoftSkillsDetailComponent;
  let fixture: ComponentFixture<TechCVSoftSkillsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechCVSoftSkillsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TechCVSoftSkillsDetailComponent,
              resolve: { techCVSoftSkills: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TechCVSoftSkillsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TechCVSoftSkillsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load techCVSoftSkills on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TechCVSoftSkillsDetailComponent);

      // THEN
      expect(instance.techCVSoftSkills()).toEqual(expect.objectContaining({ id: 123 }));
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
