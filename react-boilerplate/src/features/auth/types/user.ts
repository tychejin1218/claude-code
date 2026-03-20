import { z } from 'zod';

/** 사용자 역할 */
export const memberRoleSchema = z.enum(['ROLE_USER', 'ROLE_ADMIN']);
export type MemberRole = z.infer<typeof memberRoleSchema>;

/** 사용자 */
export const userSchema = z.object({
  userId: z.string(),
  name: z.string(),
  email: z.string(),
  role: memberRoleSchema,
});

export type User = z.infer<typeof userSchema>;
