import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tech-cv-hard-skills.test-samples';

import { TechCVHardSkillsFormService } from './tech-cv-hard-skills-form.service';

describe('TechCVHardSkills Form Service', () => {
  let service: TechCVHardSkillsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TechCVHardSkillsFormService);
  });

  describe('Service methods', () => {
    describe('createTechCVHardSkillsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTechCVHardSkillsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            skills: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });

      it('passing ITechCVHardSkills should create a new form with FormGroup', () => {
        const formGroup = service.createTechCVHardSkillsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            skills: expect.any(Object),
            technicalCV: expect.any(Object),
          }),
        );
      });
    });

    describe('getTechCVHardSkills', () => {
      it('should return NewTechCVHardSkills for default TechCVHardSkills initial value', () => {
        const formGroup = service.createTechCVHardSkillsFormGroup(sampleWithNewData);

        const techCVHardSkills = service.getTechCVHardSkills(formGroup) as any;

        expect(techCVHardSkills).toMatchObject(sampleWithNewData);
      });

      it('should return NewTechCVHardSkills for empty TechCVHardSkills initial value', () => {
        const formGroup = service.createTechCVHardSkillsFormGroup();

        const techCVHardSkills = service.getTechCVHardSkills(formGroup) as any;

        expect(techCVHardSkills).toMatchObject({});
      });

      it('should return ITechCVHardSkills', () => {
        const formGroup = service.createTechCVHardSkillsFormGroup(sampleWithRequiredData);

        const techCVHardSkills = service.getTechCVHardSkills(formGroup) as any;

        expect(techCVHardSkills).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITechCVHardSkills should not enable id FormControl', () => {
        const formGroup = service.createTechCVHardSkillsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTechCVHardSkills should disable id FormControl', () => {
        const formGroup = service.createTechCVHardSkillsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
