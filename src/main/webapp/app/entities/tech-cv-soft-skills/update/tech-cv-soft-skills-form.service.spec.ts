import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tech-cv-soft-skills.test-samples';

import { TechCVSoftSkillsFormService } from './tech-cv-soft-skills-form.service';

describe('TechCVSoftSkills Form Service', () => {
  let service: TechCVSoftSkillsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechCVSoftSkillsFormService);
  });

  describe('Service methods', () => {
    describe('createTechCVSoftSkillsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechCVSoftSkillsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            skills: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });

      it('passing ITechCVSoftSkills should create a new form with FormGroup', () => {
        const formGroup = service.createTechCVSoftSkillsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            skills: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });
    });

    describe('getTechCVSoftSkills', () => {
      it('should return NewTechCVSoftSkills for default TechCVSoftSkills initial value', () => {
        const formGroup = service.createTechCVSoftSkillsFormGroup(sampleWithNewData);

        const techCVSoftSkills = service.getTechCVSoftSkills(formGroup) as any;

        expect(techCVSoftSkills).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechCVSoftSkills for empty TechCVSoftSkills initial value', () => {
        const formGroup = service.createTechCVSoftSkillsFormGroup();

        const techCVSoftSkills = service.getTechCVSoftSkills(formGroup) as any;

        expect(techCVSoftSkills).toMatchObject({});
      });

      it('should return ITechCVSoftSkills', () => {
        const formGroup = service.createTechCVSoftSkillsFormGroup(sampleWithRequiredData);

        const techCVSoftSkills = service.getTechCVSoftSkills(formGroup) as any;

        expect(techCVSoftSkills).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechCVSoftSkills should not enable id FormControl', () => {
        const formGroup = service.createTechCVSoftSkillsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechCVSoftSkills should disable id FormControl', () => {
        const formGroup = service.createTechCVSoftSkillsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
