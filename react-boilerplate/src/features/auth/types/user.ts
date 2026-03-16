import { z } from 'zod';

/** 사용자 */
export const userSchema = z.object({
  userId: z.string(),
  name: z.string(),
  email: z.string(),
});

export type User = z.infer<typeof userSchema>;
